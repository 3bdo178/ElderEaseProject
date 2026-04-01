using ElderEaseAPI.DTOs;
using ElderEaseAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LookupsController : ControllerBase
    {
        private readonly ElderEasedbContext _context;

        public LookupsController(ElderEasedbContext context)
        {
            _context = context;
        }

        [HttpGet("status-types")]
        public async Task<ActionResult<IEnumerable<LookupItemDto>>> GetStatusTypes()
        {
            var items = await _context.StatusTypes
                .Select(x => new LookupItemDto
                {
                    Id = x.Id,
                    Name = x.Name
                })
                .ToListAsync();

            return Ok(items);
        }

        [HttpGet("repeat-types")]
        public async Task<ActionResult<IEnumerable<LookupItemDto>>> GetRepeatTypes()
        {
            var items = await _context.RepeatTypes
                .Select(x => new LookupItemDto
                {
                    Id = x.Id,
                    Name = x.Name
                })
                .ToListAsync();

            return Ok(items);
        }

        [HttpGet("detail-types")]
        public async Task<ActionResult<IEnumerable<LookupItemDto>>> GetDetailTypes()
        {
            var items = await _context.DetailTypes
                .Select(x => new LookupItemDto
                {
                    Id = x.Id,
                    Name = x.Name
                })
                .ToListAsync();

            return Ok(items);
        }
    }
}
