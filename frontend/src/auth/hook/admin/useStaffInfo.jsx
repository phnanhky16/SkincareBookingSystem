import { useQuery } from "@tanstack/react-query";
import { APIClient } from "@/lib/api-client";
import { ACTIONS } from "@/lib/api-client/constant";
import { getCookie } from "cookies-next";

export const useStaffInfo = () => {
  const token = getCookie("token");

  return useQuery({
    queryKey: ["staffInfo"], // Key này phải khớp với key trong invalidateQueries
    queryFn: async () => {
      const response = await APIClient.invoke({
        action: ACTIONS.GET_STAFF_INFO,
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response && response.success === true) {
        return response.result;
      }

      throw new Error(response.message || "Lỗi khi lấy thông tin staff");
    },
  });
};
