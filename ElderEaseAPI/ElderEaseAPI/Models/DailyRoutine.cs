using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class DailyRoutine
{
    public int Id { get; set; }

    public int? SeniorId { get; set; }

    public string? Title { get; set; }

    public string? Type { get; set; }

    public DateOnly? Date { get; set; }

    public bool? IsDone { get; set; }

    public virtual Senior? Senior { get; set; }
}
