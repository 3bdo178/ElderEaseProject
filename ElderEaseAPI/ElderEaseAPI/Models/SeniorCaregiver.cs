using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class SeniorCaregiver
{
    public int SeniorId { get; set; }

    public int CaregiverId { get; set; }

    public string? Relation { get; set; }

    public virtual Caregiver Caregiver { get; set; } = null!;

    public virtual Senior Senior { get; set; } = null!;
}
