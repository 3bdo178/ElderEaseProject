using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class DetailType
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public virtual ICollection<Detail> Details { get; set; } = new List<Detail>();
}
