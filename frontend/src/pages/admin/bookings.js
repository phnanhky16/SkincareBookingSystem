import { AdminLayout } from "@/components/Admin/AdminLayout";
import { AuthGuard } from "@/auth/AUTHGUARD/AuthGuard";

import BookingSchedule from "@/components/Admin/Booking/BookingSchedule";

const breadcrumbsData = [
  {
    label: "Admin",
    path: "/admin",
  },
  {
    label: "Lịch hẹn",
    path: "/admin/bookings",
  },
];

const BookingsPage = () => {
  return (
    <AuthGuard>
      <AdminLayout
        breadcrumb={breadcrumbsData}
        breadcrumbTitle="Quản lý lịch hẹn"
      >
        <BookingSchedule />
      </AdminLayout>
    </AuthGuard>
  );
};

export default BookingsPage;
