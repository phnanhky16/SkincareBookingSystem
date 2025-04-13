package com.skincare_booking_system.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.skincare_booking_system.constant.Roles;
import com.skincare_booking_system.dto.request.AuthenticationRequest;
import com.skincare_booking_system.dto.request.IntrospectRequest;
import com.skincare_booking_system.dto.request.LogoutRequest;
import com.skincare_booking_system.dto.request.RefreshRequest;
import com.skincare_booking_system.dto.response.AuthenticationResponse;
import com.skincare_booking_system.dto.response.IntrospectResponse;
import com.skincare_booking_system.entities.InvalidatedToken;
import com.skincare_booking_system.entities.Staff;
import com.skincare_booking_system.entities.Therapist;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.InvalidatedTokenRepository;
import com.skincare_booking_system.repository.StaffRepository;
import com.skincare_booking_system.repository.TherapistRepository;
import com.skincare_booking_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationService {
    UserRepository userRepository;
    TherapistRepository therapistRepository;
    StaffRepository staffRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // de bien nay khong add vao constructor
    @Value("${jwt.signerKey}") // doc signerKey tu file yaml
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        String username = request.getUsername();

        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findUserByUsername(username);
            if (!user.getStatus()) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            if (!new BCryptPasswordEncoder(10).matches(request.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            var token = generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .role(user.getRole())
                    .success(true)
                    .build(); // Trả về ngay sau khi thành công
        }

        if (therapistRepository.existsByUsername(username)) {
            Therapist therapist = therapistRepository.findTherapistByUsername(username);
            if (!therapist.getStatus()) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            if (!new BCryptPasswordEncoder(10).matches(request.getPassword(), therapist.getPassword())) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            var token = generateTokenThe(therapist);
            return AuthenticationResponse.builder()
                    .token(token)
                    .role(therapist.getRole())
                    .success(true)
                    .build();
        }

        if (staffRepository.existsByUsername(username)) {
            Staff staff = staffRepository.findStaffByUsername(username);
            if (!staff.getStatus()) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            if (!new BCryptPasswordEncoder(10).matches(request.getPassword(), staff.getPassword())) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            var token = generateTokenSta(staff);
            return AuthenticationResponse.builder()
                    .token(token)
                    .role(staff.getRole())
                    .success(true)
                    .build();
        }
        throw new AppException(ErrorCode.USER_NOT_EXISTED); // Không tìm thấy tài khoản nào
    }

    public AuthenticationResponse loginGoogle(String token) {
        log.info("LoginGG token: {}", token);
        try {
            log.info("TRY catch: ");
            FirebaseToken decodeToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodeToken.getEmail();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setFirstName(decodeToken.getName());
                newUser.setRole(Roles.CUSTOMER);
                newUser.setStatus(true);
                user = userRepository.save(newUser);
            }
            if (!user.getStatus()) {
                throw new AppException(ErrorCode.CUSTOMER_DE_ACTIVE);
            }
            log.info("response ");
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(generateToken(user));
            response.setRole(user.getRole());
            return response;
        } catch (FirebaseAuthException e) {
            log.info("FirebaseAuthException");
            e.printStackTrace();
        }
        return null;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // create for set in JWSObject

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("SkinCareBooking")
                .issueTime(new Date())
                .claim("role", user.getRole().name())
                .expirationTime(
                        new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // tao payload

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error while signing JWT object", e);
            throw new RuntimeException(e);
        }
    }

    //    private String buildScope(User user) {
    //        StringJoiner stringJoiner = new StringJoiner(" "); // Ghép nhiều role thành chuỗi
    //        if (!CollectionUtils.isEmpty(user.getRole())) {
    //            user.getRole().name(role -> stringJoiner.add("ROLE_" + ()));
    //        }
    //        return stringJoiner.toString();
    //    }

    private String generateTokenThe(Therapist therapist) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // create for set in JWSObject

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(therapist.getUsername())
                .issuer("SkinCareBooking")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // tao payload

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error while signing JWT object", e);
            throw new RuntimeException(e);
        }
    }

    //    private String buildScopeThe(Therapist therapist) {
    //        StringJoiner stringJoiner = new StringJoiner(" "); // Ghép nhiều role thành chuỗi
    //        if (!CollectionUtils.isEmpty(therapist.getRoles())) {
    //            therapist.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
    //        }
    //        return stringJoiner.toString();
    //    }

    private String generateTokenSta(Staff staff) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // create for set in JWSObject

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(staff.getUsername())
                .issuer("SkinCareBooking")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // tao payload

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error while signing JWT object", e);
            throw new RuntimeException(e);
        }
    }

    //    private String buildScopeSta(Staff staff) {
    //        StringJoiner stringJoiner = new StringJoiner(" "); // Ghép nhiều role thành chuỗi
    //        if (!CollectionUtils.isEmpty(staff.getRole())) {
    //            staff.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
    //        }
    //        return stringJoiner.toString();
    //    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJwt = verifyToken(request.getToken());

        var jwtId = signJwt.getJWTClaimsSet().getJWTID();
        var expirationTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATION));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).success(true).build();
    }

    public AuthenticationResponse refreshTokenThe(RefreshRequest request) throws ParseException, JOSEException {
        var signJwt = verifyToken(request.getToken());

        var jwtId = signJwt.getJWTClaimsSet().getJWTID();
        var expirationTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();
        var user = therapistRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATION));

        var token = generateTokenThe(user);

        return AuthenticationResponse.builder().token(token).success(true).build();
    }

    public AuthenticationResponse refreshTokenStaff(RefreshRequest request) throws ParseException, JOSEException {
        var signJwt = verifyToken(request.getToken());

        var jwtId = signJwt.getJWTClaimsSet().getJWTID();
        var expirationTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();
        var user = staffRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATION));

        var token = generateTokenSta(user);

        return AuthenticationResponse.builder().token(token).success(true).build();
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATION);

        return signedJWT;
    }
}
