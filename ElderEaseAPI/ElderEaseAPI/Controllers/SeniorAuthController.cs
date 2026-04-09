using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SeniorAuthController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public SeniorAuthController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpPost("login")]
        public IActionResult Login(SeniorLoginDto dto)
        {
            var senior = _context.Seniors
                .FirstOrDefault(s => s.Email == dto.Email && s.Password == dto.Password);

            if (senior == null)
                return Unauthorized("Invalid email or password");

            return Ok(new
            {
                Message = "Login successful",
                SeniorId = senior.Id,
                Name = senior.Name,
                Email = senior.Email
            });
        }
        [HttpPost("register")]
        public async Task<ActionResult<SeniorRegisterResponseDto>> Register(SeniorRegisterDto dto)
        {
            var existingSenior = await _context.Seniors
                .FirstOrDefaultAsync(s => s.Email == dto.Email);

            if (existingSenior != null)
            {
                return BadRequest("This email is already registered.");
            }

            var senior = new Senior
            {
                Name = dto.Name,
                Email = dto.Email,
                Password = dto.Password, // simple for now as we agreed
                Phone = dto.Phone,
                Location = dto.Location,
                EmergencyContactName = dto.EmergencyContactName,
                EmergencyContactPhone = dto.EmergencyContacPhone,
                Dob = dto.DOB
            };

            _context.Seniors.Add(senior);
            await _context.SaveChangesAsync();

            var response = new SeniorRegisterResponseDto
            {
                SeniorId = senior.Id,
                Name = senior.Name,
                Email = senior.Email,
                Message = "Senior registered successfully"
            };

            return Ok(response);
        }
    }
}