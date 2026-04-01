using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class Exercise
{
    public int Id { get; set; }

    public string? Title { get; set; }

    public string? Description { get; set; }

    public int? Duration { get; set; }

    public string? Category { get; set; }

    public string? DifficultyLevel { get; set; }
}
