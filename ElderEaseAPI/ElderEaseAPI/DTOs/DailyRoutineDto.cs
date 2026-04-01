namespace ElderEaseAPI.DTOs
{
    public class DailyRoutineDto
    {
        public int Id { get; set; }
        public int? SeniorId { get; set; }
        public string? Title { get; set; } = null!;
        public string? Type { get; set; } = null!;
        public DateOnly? Date { get; set; }
        public bool IsDone { get; set; }
    }
}
