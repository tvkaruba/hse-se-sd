using HotChocolate.Data;
using Microsoft.EntityFrameworkCore;
using Pacman.Web.Api.Controllers.Models;
using Pacman.Web.Api.Dal;

namespace Pacman.Web.Api.Queries;

[ExtendObjectType(OperationTypeNames.Query)]
[GraphQLDescription("Query games")]
public sealed class SessionInfoQuery
{
    [GraphQLDescription("Get list of games")]
    [UsePaging]
    [UseProjection]
    [UseFiltering]
    [UseSorting]
    public IQueryable<SessionInfoDto> GetGames([Service] DataContext context)
    {
        return context.Sessions
           .AsNoTracking()
           .TagWith($"{nameof(SessionInfoQuery)}::{nameof(GetGames)}")
           .OrderByDescending(game => game.CreatedAt)
           .Include(game => game.TickStates)
           .Select(game => new SessionInfoDto
           {
               Id = game.Id,
               Title = game.Title,
               TickStates = game.TickStates.Select(gti => new TickStateDto
               {
                   Id = gti.Id,
                   TickSnapshot = gti.TickSnapshot,
                   CreatedAt = gti.CreatedAt,
               }),
           });
    }
}
