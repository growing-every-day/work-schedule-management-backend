package fastcampus.workschedulemanagementbackend.dto.response.useraccount;

public record UserAccountDeleteResponse(Boolean deleted) {

        public static UserAccountDeleteResponse of(Boolean deleted) {
            return new UserAccountDeleteResponse(deleted);
        }
}
