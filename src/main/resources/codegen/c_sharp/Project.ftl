<Project Sdk="Microsoft.NET.Sdk">

	<PropertyGroup>
		<OutputType>Exe</OutputType>
		<TargetFramework>${sdkVersion}</TargetFramework>
		<ImplicitUsings>enable</ImplicitUsings>
		<Nullable>enable</Nullable>
	</PropertyGroup>

	<ItemGroup>
		<PackageReference Include="Vlingo.Xoom.Actors" Version="${xoomVersion}" />
		<PackageReference Include="Vlingo.Xoom.Symbio" Version="${xoomVersion}" />
		<PackageReference Include="Vlingo.Xoom.Lattice" Version="${xoomVersion}" />
	</ItemGroup>
	<ItemGroup>
		<Content Include="vlingo-actors.json">
			<CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
		</Content>
	</ItemGroup>
</Project>
