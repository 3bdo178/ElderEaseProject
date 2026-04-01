namespace ElderEaseAPI.DTOs
{
    public class CreateHealthEntryDto
    {
        public int SeniorId { get; set; }
        public string Title { get; set; } = null!;
        public string? Notes { get; set; }
        public TimeOnly Time { get; set; }
        public DateOnly Date { get; set; }
        public List<CreateDetailDto> Details { get; set; } = new();
    }
}
