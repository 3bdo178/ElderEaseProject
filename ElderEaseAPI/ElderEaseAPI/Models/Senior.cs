using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class Senior
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public string? Email { get; set; }

    public string? Password { get; set; }

    public string? Phone { get; set; }

    public string? Location { get; set; }

    public string? EmergencyContactName { get; set; }

    public string? EmergencyContactPhone { get; set; }

    public DateOnly? Dob { get; set; }

    public virtual ICollection<DailyRoutine> DailyRoutines { get; set; } = new List<DailyRoutine>();

    public virtual ICollection<HealthEntry> HealthEntries { get; set; } = new List<HealthEntry>();

    public virtual ICollection<Reminder> Reminders { get; set; } = new List<Reminder>();

    public virtual ICollection<SeniorCaregiver> SeniorCaregivers { get; set; } = new List<SeniorCaregiver>();
}
