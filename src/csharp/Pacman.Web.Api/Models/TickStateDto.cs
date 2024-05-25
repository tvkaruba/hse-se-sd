using HotChocolate;

namespace Pacman.Web.Api.Controllers.Models;

[GraphQLDescription("Represents basic game tick information")]
[GraphQLName("TickState")]
public sealed record TickStateDto
{
    public Guid Id { get; set; }

    public int? TickNumber { get; set; }

    public string? TickSnapshot { get; set; }

    public DateTime? CreatedAt { get; set; }
}
