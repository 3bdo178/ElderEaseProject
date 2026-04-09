namespace ElderEaseAPI.Models
{
    public class SeniorRegisterResponseDto
    {
        public int SeniorId { get; set; }
        public string Name { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Message { get; set; } = null!;
    }
}
