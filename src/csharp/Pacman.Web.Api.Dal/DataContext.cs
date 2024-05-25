using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Pacman.Web.Api.Dal.Models;
using System.Reflection;
using System.Reflection.Emit;

namespace Pacman.Web.Api.Dal;

public class DataContext : IdentityDbContext<IdentityUser<Guid>, IdentityRole<Guid>, Guid>
{
    public DbSet<SessionInfo> Sessions { get; set; } = null!;

    public DbSet<LevelInfo> Levels { get; set; } = null!;

    public DbSet<TickState> TickStates { get; set; } = null!;

    public DataContext(DbContextOptions<DataContext> options)
        : base(options)
    {
    }

    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
        builder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());

        var hasher = new PasswordHasher<IdentityUser<Guid>>();
        var id = Guid.Parse("8e445865-a24d-4543-a6c6-9443d048cdb9");
        builder.Entity<IdentityUser<Guid>>().HasData(
            new IdentityUser<Guid>
            {
                Id = id,
                Email = "myuser@user.com",
                PasswordHash = hasher.HashPassword(null, "Pa$$w0rd")
            }
        );

        var sessionId = Guid.Parse("8e445865-a24d-4543-a6c6-9443d048cd10");
        builder.Entity<SessionInfo>()
            .HasData(new SessionInfo
            {
                Id = sessionId,
                CreatedBy = id,
                CreatedAt = DateTime.UtcNow,
                Title = "test",
            });

        builder.Entity<TickState>()
            .HasData(new TickState
            {
                Id = Guid.Parse("8e445865-a24d-4543-a6c6-9443d048cd11"),
                CreatedAt = DateTime.UtcNow,
                TickSnapshot = "12345",
                TickNumber = 1,
                SessionId = sessionId,
            },
            new TickState
            {
                Id = Guid.Parse("8e445865-a24d-4543-a6c6-9443d048cd12"),
                CreatedAt = DateTime.UtcNow,
                TickSnapshot = "12346",
                TickNumber = 2,
                SessionId = sessionId,
            });
    }
}
