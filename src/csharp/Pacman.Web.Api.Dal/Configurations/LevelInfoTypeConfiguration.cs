using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Pacman.Web.Api.Dal.Configurations.Abstractions;
using Pacman.Web.Api.Dal.Models;

namespace Pacman.Web.Api.Dal.Configurations;

public class LevelInfoTypeConfiguration : EntityTypeConfigurationBase<LevelInfo, Guid>
{
    protected override void ConfigureEntity(EntityTypeBuilder<LevelInfo> builder)
    {
        builder.Property(_ => _.Map).IsRequired();
        builder.Property(_ => _.CreatedBy).IsRequired();
        builder.Property(_ => _.CreatedAt).IsRequired();
        builder.Property(_ => _.UpdatedBy);
        builder.Property(_ => _.UpdatedAt);
    }
}
