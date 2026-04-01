namespace ElderEaseAPI.DTOs
{
    public class UpdateDailyRoutineDto
    {
        public string Title { get; set; } = null!;
        public string Type { get; set; } = null!;
        public DateOnly Date { get; set; }
        public bool IsDone { get; set; }
    }
}
