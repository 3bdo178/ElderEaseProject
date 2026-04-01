using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class Detail
{
    public int Id { get; set; }

    public int? DetailTypeId { get; set; }

    public string? Value { get; set; }

    public int? HealthEntryId { get; set; }

    public virtual DetailType? DetailType { get; set; }

    public virtual HealthEntry? HealthEntry { get; set; }
}
