using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SeniorsController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public SeniorsController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<SeniorDto>> GetSeniorById(int id)
        {
            var senior = await _context.Seniors.FindAsync(id);

            if (senior == null)
                return NotFound();

            return Ok(new SeniorDto
            {
                Id = senior.Id,
                Name = senior.Name,
                Email = senior.Email,
                Phone = senior.Phone,
                Location = senior.Location,
                EmergencyContactName = senior.EmergencyContactName,
                EmergencyContacPhone = senior.EmergencyContactPhone,
                DOB = senior.Dob
            });
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateSenior(int id, UpdateSeniorDto dto)
        {
            var senior = await _context.Seniors.FindAsync(id);

            if (senior == null)
                return NotFound();

            senior.Name = dto.Name;
            senior.Phone = dto.Phone;
            senior.Location = dto.Location;
            senior.EmergencyContactName = dto.EmergencyContactName;
            senior.EmergencyContactPhone = dto.EmergencyContactPhone;
            senior.Dob = dto.DOB;

            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}

