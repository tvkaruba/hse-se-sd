using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Pacman.Web.Api.Dal.Configurations.Abstractions;
using Pacman.Web.Api.Dal.Models;

namespace Pacman.Web.Api.Dal.Configurations;

public class TickStateConfiguration : EntityTypeConfigurationBase<TickState, Guid>
{
    protected override void ConfigureEntity(EntityTypeBuilder<TickState> builder)
    {
        builder.Property(_ => _.TickSnapshot).IsRequired();
        builder.Property(_ => _.TickNumber).IsRequired();
        builder.Property(_ => _.CreatedAt).IsRequired();

        builder.HasOne(_ => _.Session).WithMany(_ => _.TickStates);

        builder.HasIndex(_ => _.SessionId);
    }
}
