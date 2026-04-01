namespace ElderEaseAPI.DTOs
{
   
        namespace ElderEaseAPI.DTOs.Exercise
    {
        public class ExerciseDto
        {
            public int Id { get; set; }
            public string? Title { get; set; } = null!;
            public string? Description { get; set; } = null!;
            public int? Duration { get; set; }
            public string? Category { get; set; } = null!;
            public string? DifficultyLevel { get; set; } = null!;
        }
    }
}
