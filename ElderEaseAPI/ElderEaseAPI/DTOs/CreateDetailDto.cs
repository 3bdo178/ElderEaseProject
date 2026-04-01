namespace ElderEaseAPI.DTOs
{
    public class CreateDetailDto
    {
        public int DetailTypeId { get; set; }
        public string Value { get; set; } = null!;
    }
}
