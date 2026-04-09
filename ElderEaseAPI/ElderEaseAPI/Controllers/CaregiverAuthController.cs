using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CaregiverAuthController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public CaregiverAuthController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpPost("login")]
        public IActionResult Login(CaregiverLoginDto dto)
        {
            var caregiver = _context.Caregivers
                .FirstOrDefault(c => c.Email == dto.Email && c.Password == dto.Password);

            if (caregiver == null)
                return Unauthorized("Invalid email or password");

            return Ok(new
            {
                Message = "Login successful",
                CaregiverId = caregiver.Id,
                Name = caregiver.Name,
                Email = caregiver.Email
            });
        }
        [HttpPost("register")]
        public async Task<ActionResult<CaregiverRegisterResponseDto>> Register(CaregiverRegisterDto dto)
        {
            var existingCaregiver = await _context.Caregivers
                .FirstOrDefaultAsync(c => c.Email == dto.Email);

            if (existingCaregiver != null)
            {
                return BadRequest("This email is already registered.");
            }

            var caregiver = new Caregiver
            {
                Name = dto.Name,
                Email = dto.Email,
                Password = dto.Password, // simple for now as we agreed
                Phone = dto.Phone
            };

            _context.Caregivers.Add(caregiver);
            await _context.SaveChangesAsync();

            var response = new CaregiverRegisterResponseDto
            {
                CaregiverId = caregiver.Id,
                Name = caregiver.Name,
                Email = caregiver.Email,
                Message = "Caregiver registered successfully"
            };

            return Ok(response);
        }
    }
}