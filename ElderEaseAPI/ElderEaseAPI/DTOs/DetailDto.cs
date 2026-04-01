namespace ElderEaseAPI.DTOs
{
    public class DetailDto
    {
        public int Id { get; set; }
        public int? DetailTypeId { get; set; }
        public string? DetailTypeName { get; set; } = null!;
        public string? Value { get; set; } = null!;
    }
}
