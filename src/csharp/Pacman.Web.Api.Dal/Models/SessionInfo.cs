using Microsoft.AspNetCore.Identity;
using Pacman.Web.Api.Dal.Models.Abstractions;

namespace Pacman.Web.Api.Dal.Models;

public class SessionInfo : MutableEntityBase<Guid>
{
    public string Title { get; set; } = string.Empty;

    [UseFiltering]
    [UseSorting]
    public ICollection<TickState> TickStates { get; set; } = [];
}
