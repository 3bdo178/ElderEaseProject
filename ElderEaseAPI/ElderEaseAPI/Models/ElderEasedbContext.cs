using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace ElderEaseAPI.Models;

public partial class ElderEasedbContext : DbContext
{
    public ElderEasedbContext()
    {
    }

    public ElderEasedbContext(DbContextOptions<ElderEasedbContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Caregiver> Caregivers { get; set; }

    public virtual DbSet<DailyRoutine> DailyRoutines { get; set; }

    public virtual DbSet<Detail> Details { get; set; }

    public virtual DbSet<DetailType> DetailTypes { get; set; }

    public virtual DbSet<Exercise> Exercises { get; set; }

    public virtual DbSet<HealthEntry> HealthEntries { get; set; }

    public virtual DbSet<Reminder> Reminders { get; set; }

    public virtual DbSet<RepeatType> RepeatTypes { get; set; }

    public virtual DbSet<Senior> Seniors { get; set; }

    public virtual DbSet<SeniorCaregiver> SeniorCaregivers { get; set; }

    public virtual DbSet<StatusType> StatusTypes { get; set; }

    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Caregiver>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Caregive__3214EC0700052CDA");

            entity.ToTable("Caregiver");

            entity.HasIndex(e => e.Email, "UQ__Caregive__A9D10534AC9F0BFF").IsUnique();

            entity.Property(e => e.Email).HasMaxLength(100);
            entity.Property(e => e.Name).HasMaxLength(100);
            entity.Property(e => e.Password).HasMaxLength(255);
            entity.Property(e => e.Phone).HasMaxLength(20);
        });

        modelBuilder.Entity<DailyRoutine>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DailyRou__3214EC07CB98F5FA");

            entity.ToTable("DailyRoutine");

            entity.Property(e => e.Title).HasMaxLength(100);
            entity.Property(e => e.Type).HasMaxLength(50);

            entity.HasOne(d => d.Senior).WithMany(p => p.DailyRoutines)
                .HasForeignKey(d => d.SeniorId)
                .HasConstraintName("FK__DailyRout__Senio__6754599E");
        });

        modelBuilder.Entity<Detail>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Details__3214EC070346D01D");

            entity.Property(e => e.Value).HasMaxLength(50);

            entity.HasOne(d => d.DetailType).WithMany(p => p.Details)
                .HasForeignKey(d => d.DetailTypeId)
                .HasConstraintName("FK__Details__DetailT__628FA481");

            entity.HasOne(d => d.HealthEntry).WithMany(p => p.Details)
                .HasForeignKey(d => d.HealthEntryId)
                .HasConstraintName("FK__Details__HealthE__619B8048");
        });

        modelBuilder.Entity<DetailType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DetailTy__3214EC079B59B50B");

            entity.ToTable("DetailType");

            entity.Property(e => e.Name).HasMaxLength(50);
        });

        modelBuilder.Entity<Exercise>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Exercise__3214EC076566BF73");

            entity.Property(e => e.Category).HasMaxLength(50);
            entity.Property(e => e.Description).HasMaxLength(255);
            entity.Property(e => e.DifficultyLevel).HasMaxLength(50);
            entity.Property(e => e.Title).HasMaxLength(100);
        });

        modelBuilder.Entity<HealthEntry>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__HealthEn__3214EC07C574E896");

            entity.ToTable("HealthEntry");

            entity.Property(e => e.Notes).HasMaxLength(255);
            entity.Property(e => e.Title).HasMaxLength(100);

            entity.HasOne(d => d.Senior).WithMany(p => p.HealthEntries)
                .HasForeignKey(d => d.SeniorId)
                .HasConstraintName("FK__HealthEnt__Senio__5EBF139D");
        });

        modelBuilder.Entity<Reminder>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Reminder__3214EC075F612978");

            entity.ToTable("Reminder");

            entity.Property(e => e.Description).HasMaxLength(255);
            entity.Property(e => e.Title).HasMaxLength(100);

            entity.HasOne(d => d.RepeatType).WithMany(p => p.Reminders)
                .HasForeignKey(d => d.RepeatTypeId)
                .HasConstraintName("FK__Reminder__Repeat__59FA5E80");

            entity.HasOne(d => d.Senior).WithMany(p => p.Reminders)
                .HasForeignKey(d => d.SeniorId)
                .HasConstraintName("FK__Reminder__Senior__59063A47");

            entity.HasOne(d => d.StatusType).WithMany(p => p.Reminders)
                .HasForeignKey(d => d.StatusTypeId)
                .HasConstraintName("FK__Reminder__Status__5812160E");
        });

        modelBuilder.Entity<RepeatType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__RepeatTy__3214EC07B9CD9D6A");

            entity.ToTable("RepeatType");

            entity.Property(e => e.Name).HasMaxLength(50);
        });

        modelBuilder.Entity<Senior>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Senior__3214EC07DC8ECEB5");

            entity.ToTable("Senior");

            entity.HasIndex(e => e.Email, "UQ__Senior__A9D1053473B244D3").IsUnique();

            entity.Property(e => e.Dob).HasColumnName("DOB");
            entity.Property(e => e.Email).HasMaxLength(100);
            entity.Property(e => e.EmergencyContactName).HasMaxLength(100);
            entity.Property(e => e.EmergencyContactPhone).HasMaxLength(50);
            entity.Property(e => e.Location).HasMaxLength(255);
            entity.Property(e => e.Name).HasMaxLength(100);
            entity.Property(e => e.Password).HasMaxLength(255);
            entity.Property(e => e.Phone).HasMaxLength(50);
        });

        modelBuilder.Entity<SeniorCaregiver>(entity =>
        {
            entity.HasKey(e => new { e.SeniorId, e.CaregiverId }).HasName("PK__SeniorCa__4BD01EA70A6D2500");

            entity.ToTable("SeniorCaregiver");

            entity.Property(e => e.Relation).HasMaxLength(50);

            entity.HasOne(d => d.Caregiver).WithMany(p => p.SeniorCaregivers)
                .HasForeignKey(d => d.CaregiverId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__SeniorCar__Careg__5165187F");

            entity.HasOne(d => d.Senior).WithMany(p => p.SeniorCaregivers)
                .HasForeignKey(d => d.SeniorId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__SeniorCar__Senio__5070F446");
        });

        modelBuilder.Entity<StatusType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__StatusTy__3213E83F98053AFA");

            entity.ToTable("StatusType");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Name).HasMaxLength(50);
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
