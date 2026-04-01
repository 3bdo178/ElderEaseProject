namespace ElderEaseAPI.DTOs
{
    public class UpdateReminderDto
    {
        public int RepeatTypeId { get; set; }
        public string Title { get; set; } = null!;
        public string? Description { get; set; }
        public int StatusTypeId { get; set; }
        public DateOnly Date { get; set; }
        public TimeOnly RemindAt { get; set; }
    }
}
