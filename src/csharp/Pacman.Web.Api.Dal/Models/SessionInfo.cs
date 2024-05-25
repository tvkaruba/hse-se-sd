using Microsoft.AspNetCore.Identity;
using Pacman.Web.Api.Dal.Models.Abstractions;

namespace Pacman.Web.Api.Dal.Models;

public class SessionInfo : MutableEntityBase<Guid>
{
    public string Title { get; set; } = string.Empty;

    public Guid UserId { get; set; }

    public IdentityUser<Guid>? User { get; set; }

    public Guid LevelId { get; set; }

    public LevelInfo? Level { get; set; }

    [UseFiltering]
    [UseSorting]
    public ICollection<TickState> TickStates { get; set; } = [];
}
