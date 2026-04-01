namespace ElderEaseAPI.DTOs
{
    public class ReminderDto
    {
        public int Id { get; set; }
        public int? SeniorId { get; set; }
        public string? Title { get; set; }
        public string? Description { get; set; }
        public int? RepeatTypeId { get; set; }
        public string? RepeatTypeName { get; set; }
        public int? StatusTypeId { get; set; }
        public string? StatusTypeName { get; set; }
        public DateOnly? Date { get; set; }
        public TimeOnly? RemindAt { get; set; }
    }
}
