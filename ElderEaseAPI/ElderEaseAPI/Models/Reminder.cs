using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class Reminder
{
    public int Id { get; set; }

    public int? SeniorId { get; set; }

    public int? RepeatTypeId { get; set; }

    public string? Title { get; set; }

    public string? Description { get; set; }

    public int? StatusTypeId { get; set; }

    public DateOnly? Date { get; set; }

    public TimeOnly? RemindAt { get; set; }

    public virtual RepeatType? RepeatType { get; set; }

    public virtual Senior? Senior { get; set; }

    public virtual StatusType? StatusType { get; set; }
}
