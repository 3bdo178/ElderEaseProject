using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DailyRoutinesController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public DailyRoutinesController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("senior/{seniorId}")]
        public async Task<ActionResult<IEnumerable<DailyRoutineDto>>> GetDailyRoutinesBySenior(int seniorId)
        {
            var routines = await _context.DailyRoutines
                .Where(r => r.SeniorId == seniorId)
                .Select(r => new DailyRoutineDto
                {
                    Id = r.Id,
                    SeniorId = r.SeniorId,
                    Title = r.Title,
                    Type = r.Type,
                    Date = r.Date,
                    IsDone = r.IsDone ?? false
                })
                .ToListAsync();

            return Ok(routines);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<DailyRoutineDto>> GetDailyRoutineById(int id)
        {
            var routine = await _context.DailyRoutines.FindAsync(id);

            if (routine == null)
                return NotFound();

            return Ok(new DailyRoutineDto
            {
                Id = routine.Id,
                SeniorId = routine.SeniorId,
                Title = routine.Title,
                Type = routine.Type,
                Date = routine.Date,
                IsDone = routine.IsDone ?? false
            });
        }

        [HttpPost]
        public async Task<IActionResult> CreateDailyRoutine(CreateDailyRoutineDto dto)
        {
            var routine = new DailyRoutine
            {
                SeniorId = dto.SeniorId,
                Title = dto.Title,
                Type = dto.Type,
                Date = dto.Date,
                IsDone = dto.IsDone
            };

            _context.DailyRoutines.Add(routine);
            await _context.SaveChangesAsync();

            return Ok(routine.Id);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateDailyRoutine(int id, UpdateDailyRoutineDto dto)
        {
            var routine = await _context.DailyRoutines.FindAsync(id);

            if (routine == null)
                return NotFound();

            routine.Title = dto.Title;
            routine.Type = dto.Type;
            routine.Date = dto.Date;
            routine.IsDone = dto.IsDone;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpPatch("{id}/toggle")]
        public async Task<IActionResult> ToggleRoutineStatus(int id)
        {
            var routine = await _context.DailyRoutines.FindAsync(id);

            if (routine == null)
                return NotFound();

            routine.IsDone = !(routine.IsDone ?? false);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDailyRoutine(int id)
        {
            var routine = await _context.DailyRoutines.FindAsync(id);

            if (routine == null)
                return NotFound();

            _context.DailyRoutines.Remove(routine);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}
