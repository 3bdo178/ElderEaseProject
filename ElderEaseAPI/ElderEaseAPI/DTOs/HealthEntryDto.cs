namespace ElderEaseAPI.DTOs
{
    public class HealthEntryDto
    {
        public int Id { get; set; }
        public int? SeniorId { get; set; }
        public string? Title { get; set; } = null!;
        public string? Notes { get; set; }
        public TimeOnly? Time { get; set; }
        public DateOnly? Date { get; set; }
    }
}
