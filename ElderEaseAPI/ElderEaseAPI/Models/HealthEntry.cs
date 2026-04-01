using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class HealthEntry
{
    public int Id { get; set; }

    public int? SeniorId { get; set; }

    public string? Title { get; set; }

    public string? Notes { get; set; }

    public TimeOnly? Time { get; set; }

    public DateOnly? Date { get; set; }

    public virtual ICollection<Detail> Details { get; set; } = new List<Detail>();

    public virtual Senior? Senior { get; set; }
}
