using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CaregiversController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public CaregiversController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<CaregiverDto>> GetCaregiverById(int id)
        {
            var caregiver = await _context.Caregivers.FindAsync(id);

            if (caregiver == null)
                return NotFound();

            return Ok(new CaregiverDto
            {
                Id = caregiver.Id,
                Name = caregiver.Name,
                Email = caregiver.Email,
                Phone = caregiver.Phone
            });
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateCaregiver(int id, UpdateCaregiverDto dto)
        {
            var caregiver = await _context.Caregivers.FindAsync(id);

            if (caregiver == null)
                return NotFound();

            caregiver.Name = dto.Name;
            caregiver.Phone = dto.Phone;

            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpGet("{caregiverId}/seniors")]
        public async Task<IActionResult> GetSeniorsForCaregiver(int caregiverId)
        {
            var seniors = await _context.SeniorCaregivers
                .Where(sc => sc.CaregiverId == caregiverId)
                .Select(sc => new
                {
                    sc.Senior.Id,
                    sc.Senior.Name,
                    sc.Senior.Email,
                    sc.Senior.Phone,
                    sc.Relation
                }).ToListAsync();

            return Ok(seniors);
        }
    }
}