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
    }
}