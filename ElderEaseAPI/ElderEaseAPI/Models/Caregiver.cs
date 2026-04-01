using System;
using System.Collections.Generic;

namespace ElderEaseAPI.Models;

public partial class Caregiver
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public string? Email { get; set; }

    public string? Password { get; set; }

    public string? Phone { get; set; }

    public virtual ICollection<SeniorCaregiver> SeniorCaregivers { get; set; } = new List<SeniorCaregiver>();
}
