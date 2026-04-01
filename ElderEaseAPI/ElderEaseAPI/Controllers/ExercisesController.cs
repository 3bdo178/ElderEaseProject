using ElderEaseAPI.DTOs.ElderEaseAPI.DTOs.Exercise;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExercisesController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public ExercisesController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ExerciseDto>>> GetAllExercises()
        {
            var exercises = await _context.Exercises
                .Select(e => new ExerciseDto
                {
                    Id = e.Id,
                    Title = e.Title,
                    Description = e.Description,
                    Duration = e.Duration,
                    Category = e.Category,
                    DifficultyLevel = e.DifficultyLevel
                })
                .ToListAsync();

            return Ok(exercises);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ExerciseDto>> GetExerciseById(int id)
        {
            var exercise = await _context.Exercises.FindAsync(id);

            if (exercise == null)
                return NotFound();

            return Ok(new ExerciseDto
            {
                Id = exercise.Id,
                Title = exercise.Title,
                Description = exercise.Description,
                Duration = exercise.Duration,
                Category = exercise.Category,
                DifficultyLevel = exercise.DifficultyLevel
            });
        }
    }
}