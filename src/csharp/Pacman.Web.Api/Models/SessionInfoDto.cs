using HotChocolate.Data;

namespace Pacman.Web.Api.Controllers.Models;

[GraphQLDescription("Basic game session information")]
[GraphQLName("SessionInfo")]
public sealed record SessionInfoDto
{
    public Guid Id { get; set; }

    public string? Title { get; set; } = string.Empty;

    [GraphQLDescription("A list of game ticks info")]
    [UseFiltering]
    [UseSorting]
    public IEnumerable<TickStateDto> TickStates { get; set; } = [];
}
