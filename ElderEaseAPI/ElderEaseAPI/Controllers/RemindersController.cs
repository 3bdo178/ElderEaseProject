using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RemindersController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public RemindersController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("senior/{seniorId}")]
        public async Task<ActionResult<IEnumerable<ReminderDto>>> GetRemindersBySenior(int seniorId)
        {
            var reminders = await _context.Reminders
                .Where(r => r.SeniorId == seniorId)
                .Include(r => r.RepeatType)
                .Include(r => r.StatusType)
                .Select(r => new ReminderDto
                {
                    Id = r.Id,
                    SeniorId = r.SeniorId,
                    Title = r.Title,
                    Description = r.Description,
                    RepeatTypeId = r.RepeatTypeId,
                    RepeatTypeName = r.RepeatType.Name,
                    StatusTypeId = r.StatusTypeId,
                    StatusTypeName = r.StatusType.Name,
                    Date = r.Date,
                    RemindAt = r.RemindAt
                })
                .ToListAsync();

            return Ok(reminders);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ReminderDto>> GetReminderById(int id)
        {
            var reminder = await _context.Reminders
                .Include(r => r.RepeatType)
                .Include(r => r.StatusType)
                .FirstOrDefaultAsync(r => r.Id == id);

            if (reminder == null)
                return NotFound();

            return Ok(new ReminderDto
            {
                Id = reminder.Id,
                SeniorId = reminder.SeniorId,
                Title = reminder.Title,
                Description = reminder.Description,
                RepeatTypeId = reminder.RepeatTypeId,
                RepeatTypeName = reminder.RepeatType.Name,
                StatusTypeId = reminder.StatusTypeId,
                StatusTypeName = reminder.StatusType.Name,
                Date = reminder.Date,
                RemindAt = reminder.RemindAt
            });
        }

        [HttpPost]
        public async Task<IActionResult> CreateReminder(CreateReminderDto dto)
        {
            var reminder = new Reminder
            {
                SeniorId = dto.SeniorId,
                RepeatTypeId = dto.RepeatTypeId,
                Title = dto.Title,
                Description = dto.Description,
                StatusTypeId = dto.StatusTypeId,
                Date = dto.Date,
                RemindAt = dto.RemindAt
            };

            _context.Reminders.Add(reminder);
            await _context.SaveChangesAsync();

            return Ok(reminder.Id);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateReminder(int id, UpdateReminderDto dto)
        {
            var reminder = await _context.Reminders.FindAsync(id);

            if (reminder == null)
                return NotFound();

            reminder.RepeatTypeId = dto.RepeatTypeId;
            reminder.Title = dto.Title;
            reminder.Description = dto.Description;
            reminder.StatusTypeId = dto.StatusTypeId;
            reminder.Date = dto.Date;
            reminder.RemindAt = dto.RemindAt;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpPatch("{id}/status/{statusTypeId}")]
        public async Task<IActionResult> UpdateReminderStatus(int id, int statusTypeId)
        {
            var reminder = await _context.Reminders.FindAsync(id);

            if (reminder == null)
                return NotFound();

            reminder.StatusTypeId = statusTypeId;
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteReminder(int id)
        {
            var reminder = await _context.Reminders.FindAsync(id);

            if (reminder == null)
                return NotFound();

            _context.Reminders.Remove(reminder);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}