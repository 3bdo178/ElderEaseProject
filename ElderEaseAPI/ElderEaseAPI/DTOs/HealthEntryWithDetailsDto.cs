namespace ElderEaseAPI.DTOs
{
    public class HealthEntryWithDetailsDto
    {
        public int Id { get; set; }
        public int? SeniorId { get; set; }
        public string? Title { get; set; } = null!;
        public string? Notes { get; set; }
        public TimeOnly? Time { get; set; }
        public DateOnly? Date { get; set; }
        public List<DetailDto> Details { get; set; } = new();
    }
}
