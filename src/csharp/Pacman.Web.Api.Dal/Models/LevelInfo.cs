using Pacman.Web.Api.Dal.Models.Abstractions;

namespace Pacman.Web.Api.Dal.Models;

public class LevelInfo : MutableEntityBase<Guid>
{
    public required string Map {  get; set; }
}
