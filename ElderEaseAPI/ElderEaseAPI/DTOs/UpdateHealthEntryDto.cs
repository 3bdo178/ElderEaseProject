namespace ElderEaseAPI.DTOs
{
    public class UpdateHealthEntryDto
    {
        public string Title { get; set; } = null!;
        public string? Notes { get; set; }
        public TimeOnly Time { get; set; }
        public DateOnly Date { get; set; }
    }
}
