using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Pacman.Web.Api.Dal.Models.Abstractions;
using System.Globalization;

namespace Pacman.Web.Api.Dal.Configurations.Abstractions;

public abstract class EntityTypeConfigurationBase<TEntity, TKey> : IEntityTypeConfiguration<TEntity>
    where TEntity : class, IEntity<TKey>
    where TKey : struct, IEquatable<TKey>
{
    public const string DefaultSchema = "public";

    public void Configure(EntityTypeBuilder<TEntity> builder)
    {
        var table = string.Format(CultureInfo.InvariantCulture, "{0}s", typeof(TEntity).Name);
        builder.ToTable(table, DefaultSchema);

        builder.HasKey(_ => _.Id);

        ConfigureEntity(builder);
    }

    protected abstract void ConfigureEntity(EntityTypeBuilder<TEntity> builder);

    protected string ManyToManyTableName(Type relatedType)
        => string.Format(CultureInfo.InvariantCulture, "{0}{1}s", typeof(TEntity).Name, relatedType.Name);
}
