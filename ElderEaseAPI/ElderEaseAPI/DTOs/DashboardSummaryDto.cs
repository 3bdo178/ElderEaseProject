namespace ElderEaseAPI.DTOs
{
    public class DashboardSummaryDto
    {
        public int TotalReminders { get; set; }
        public int TodayReminders { get; set; }
        public int MissedReminders { get; set; }
        public int TotalHealthEntries { get; set; }
        public int TodayRoutines { get; set; }
        public int CompletedTodayRoutines { get; set; }
        public int LinkedCaregivers { get; set; }
    }
}
