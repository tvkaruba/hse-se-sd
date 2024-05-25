using Pacman.Web.Api.Dal.Models.Abstractions;

namespace Pacman.Web.Api.Dal.Models;

public class TickState : EntityBase<Guid>
{
    public required int TickNumber { get; set; }

    public required string TickSnapshot { get; set; }

    public required DateTime CreatedAt { get; set; }

    public Guid SessionId { get; set; }

    public SessionInfo? Session { get; set; }
}
