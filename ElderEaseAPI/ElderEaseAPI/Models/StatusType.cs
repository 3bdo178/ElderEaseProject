using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class StatusType
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public virtual ICollection<Reminder> Reminders { get; set; } = new List<Reminder>();
}
