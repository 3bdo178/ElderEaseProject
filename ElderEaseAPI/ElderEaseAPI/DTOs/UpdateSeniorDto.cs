namespace ElderEaseAPI.DTOs
{
    public class UpdateSeniorDto
    {
        public string Name { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public string? Location { get; set; }
        public string? EmergencyContactName { get; set; }
        public string? EmergencyContactPhone { get; set; }
        public DateOnly DOB { get; set; }
    }
}
