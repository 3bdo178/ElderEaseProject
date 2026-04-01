namespace ElderEaseAPI.DTOs
{
    public class CreateReminderDto
    {
        public int SeniorId { get; set; }
        public int RepeatTypeId { get; set; }
        public string Title { get; set; } = null!;
        public string? Description { get; set; }
        public int StatusTypeId { get; set; }
        public DateOnly Date { get; set; }
        public TimeOnly RemindAt { get; set; }
    }
}
