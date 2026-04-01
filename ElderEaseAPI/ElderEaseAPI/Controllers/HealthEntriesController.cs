using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HealthEntriesController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public HealthEntriesController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("senior/{seniorId}")]
        public async Task<ActionResult<IEnumerable<HealthEntryDto>>> GetHealthEntriesBySenior(int seniorId)
        {
            var entries = await _context.HealthEntries
                .Where(h => h.SeniorId == seniorId)
                .OrderByDescending(h => h.Date)
                .ThenByDescending(h => h.Time)
                .Select(h => new HealthEntryDto
                {
                    Id = h.Id,
                    SeniorId = h.SeniorId,
                    Title = h.Title,
                    Notes = h.Notes,
                    Date = h.Date,
                    Time =h.Time
                })
                .ToListAsync();

            return Ok(entries);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<HealthEntryWithDetailsDto>> GetHealthEntryById(int id)
        {
            var entry = await _context.HealthEntries
                .Include(h => h.Details)
                    .ThenInclude(d => d.DetailType)
                .FirstOrDefaultAsync(h => h.Id == id);

            if (entry == null)
                return NotFound();

            return Ok(new HealthEntryWithDetailsDto
            {
                Id = entry.Id,
                SeniorId = entry.SeniorId,
                Title = entry.Title,
                Notes = entry.Notes,
                Date = entry.Date,
                Time = entry.Time,
                Details = entry.Details.Select(d => new DetailDto
                {
                    Id = d.Id,
                    DetailTypeId = d.DetailTypeId,
                    DetailTypeName = d.DetailType.Name,
                    Value = d.Value
                }).ToList()
            });
        }

        [HttpPost]
        public async Task<IActionResult> CreateHealthEntry(CreateHealthEntryDto dto)
        {
            var entry = new HealthEntry
            {
                SeniorId = dto.SeniorId,
                Title = dto.Title,
                Notes = dto.Notes,
                Date = dto.Date,
                Time = dto.Time
            };

            _context.HealthEntries.Add(entry);
            await _context.SaveChangesAsync();

            if (dto.Details.Any())
            {
                var details = dto.Details.Select(d => new Detail
                {
                    HealthEntryId = entry.Id,
                    DetailTypeId = d.DetailTypeId,
                    Value = d.Value
                }).ToList();

                _context.Details.AddRange(details);
                await _context.SaveChangesAsync();
            }

            return Ok(entry.Id);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateHealthEntry(int id, UpdateHealthEntryDto dto)
        {
            var entry = await _context.HealthEntries.FindAsync(id);

            if (entry == null)
                return NotFound();

            entry.Title = dto.Title;
            entry.Notes = dto.Notes;
            entry.Date = dto.Date;
            entry.Time = dto.Time;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpPost("{healthEntryId}/details")]
        public async Task<IActionResult> AddDetailToHealthEntry(int healthEntryId, CreateDetailDto dto)
        {
            var entryExists = await _context.HealthEntries.AnyAsync(h => h.Id == healthEntryId);
            if (!entryExists)
                return NotFound("Health entry not found.");

            var detail = new Detail
            {
                HealthEntryId = healthEntryId,
                DetailTypeId = dto.DetailTypeId,
                Value = dto.Value
            };

            _context.Details.Add(detail);
            await _context.SaveChangesAsync();

            return Ok(detail.Id);
        }

        [HttpDelete("details/{detailId}")]
        public async Task<IActionResult> DeleteDetail(int detailId)
        {
            var detail = await _context.Details.FindAsync(detailId);

            if (detail == null)
                return NotFound();

            _context.Details.Remove(detail);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteHealthEntry(int id)
        {
            var entry = await _context.HealthEntries
                .Include(h => h.Details)
                .FirstOrDefaultAsync(h => h.Id == id);

            if (entry == null)
                return NotFound();

            if (entry.Details.Any())
                _context.Details.RemoveRange(entry.Details);

            _context.HealthEntries.Remove(entry);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}
