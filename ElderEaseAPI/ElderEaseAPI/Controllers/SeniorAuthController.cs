using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

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
    }
}