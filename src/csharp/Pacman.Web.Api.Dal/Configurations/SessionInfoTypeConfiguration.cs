using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Pacman.Web.Api.Dal.Configurations.Abstractions;
using Pacman.Web.Api.Dal.Models;

namespace Pacman.Web.Api.Dal.Configurations;

public class SessionInfoTypeConfiguration : EntityTypeConfigurationBase<SessionInfo, Guid>
{
    protected override void ConfigureEntity(EntityTypeBuilder<SessionInfo> builder)
    {
        builder.Property(_ => _.Title).IsRequired();
        builder.Property(_ => _.CreatedBy).IsRequired();
        builder.Property(_ => _.CreatedAt).IsRequired();

        builder.HasMany(_ => _.TickStates).WithOne(_ => _.Session);
    }
}

