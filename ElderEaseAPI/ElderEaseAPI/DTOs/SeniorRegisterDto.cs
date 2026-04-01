namespace ElderEaseAPI.DTOs
{
    public class SeniorRegisterDto
    {
        public string Name { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Password { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public string? Location { get; set; }
        public string? EmergencyContactName { get; set; }
        public string? EmergencyContacPhone { get; set; }
        public DateOnly DOB { get; set; }
    }
}
