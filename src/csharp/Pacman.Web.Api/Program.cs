using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Pacman.Web.Api.Dal;
using Pacman.Web.Api.Queries;

namespace Pacman.Web.Api;

public class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        builder.Services.AddEntityFrameworkNpgsql();

        var connectionString = builder.Configuration.GetValue<string>("DefaultConnection");
        builder.Services.AddDbContext<DataContext>(options =>
        {
            options.UseNpgsql(connectionString, npgsqlOptions =>
            {
                npgsqlOptions.MigrationsAssembly(typeof(DataContext).Assembly.FullName);
                npgsqlOptions.MigrationsHistoryTable("__EFMigrationHistory", "public");
            });

            options.UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking);
            options.EnableSensitiveDataLogging(sensitiveDataLoggingEnabled: true);
        });


        // Add services to the container.
        builder.Services.AddAuthorization();
        builder.Services.AddIdentityApiEndpoints<IdentityUser<Guid>>()
            .AddEntityFrameworkStores<DataContext>();

        builder.Services
           .AddGraphQLServer()
           //.AddMutationType<SessionInfoMutation>()
           .AddQueryType()
              .AddTypeExtension<SessionInfoQuery>()
              //.AddTypeExtension<TickStateQuery>()
           .AddProjections()
           .AddFiltering()
           .AddSorting()
           .ModifyRequestOptions(options =>
           {
               options.IncludeExceptionDetails = builder.Environment.IsDevelopment();
           });

        builder.Services.AddControllers();
        // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
        builder.Services.AddEndpointsApiExplorer();
        builder.Services.AddSwaggerGen();

        var app = builder.Build();

        // Configure the HTTP request pipeline.
        if (app.Environment.IsDevelopment())
        {
            app.UseSwagger();
            app.UseSwaggerUI();
        }

        var webSocketOptions = new WebSocketOptions
        {
            KeepAliveInterval = TimeSpan.FromMinutes(2),
        };

        app.UseWebSockets(webSocketOptions);

        app.UseDefaultFiles();
        app.UseStaticFiles();

        app.UseHttpsRedirection();

        app.UseAuthorization();

        app.MapGraphQL("/graphql");

        app.MapIdentityApi<IdentityUser<Guid>>();
        app.MapControllers();

        app.Run();
    }
}
