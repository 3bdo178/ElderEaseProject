namespace ElderEaseAPI.Models
{
    public class CaregiverRegisterResponseDto
    {
        public int CaregiverId { get; set; }
        public string Name { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Message { get; set; } = null!;
    }
}
