namespace ElderEaseAPI.DTOs
{
    public class DashboardDto
    {
        public DashboardSummaryDto Summary { get; set; } = new();
        public List<ReminderDto> TodayReminders { get; set; } = new();
        public HealthEntryWithDetailsDto? LatestHealthEntry { get; set; }
        public List<DailyRoutineDto> TodayRoutines { get; set; } = new();
    }
}
